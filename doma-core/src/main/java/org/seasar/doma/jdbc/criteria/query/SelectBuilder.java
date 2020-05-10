package org.seasar.doma.jdbc.criteria.query;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.internal.jdbc.sql.PreparedSqlBuilder;
import org.seasar.doma.internal.util.Pair;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.criteria.context.Criterion;
import org.seasar.doma.jdbc.criteria.context.Join;
import org.seasar.doma.jdbc.criteria.context.JoinKind;
import org.seasar.doma.jdbc.criteria.context.OrderByItem;
import org.seasar.doma.jdbc.criteria.context.SelectContext;
import org.seasar.doma.jdbc.criteria.declaration.AggregateFunction;
import org.seasar.doma.jdbc.criteria.def.EntityDef;
import org.seasar.doma.jdbc.criteria.def.PropertyDef;
import org.seasar.doma.jdbc.criteria.option.DistinctOption;
import org.seasar.doma.jdbc.criteria.option.ForUpdateOption;

public class SelectBuilder {
  private final SelectContext context;
  private final Function<String, String> commenter;
  private final PreparedSqlBuilder buf;
  private final BuilderSupport support;

  public SelectBuilder(
      Config config,
      SelectContext context,
      Function<String, String> commenter,
      SqlLogType sqlLogType) {
    this(
        config,
        context,
        commenter,
        new PreparedSqlBuilder(config, SqlKind.SELECT, sqlLogType),
        new AliasManager(context));
  }

  public SelectBuilder(
      Config config,
      SelectContext context,
      Function<String, String> commenter,
      PreparedSqlBuilder buf,
      AliasManager aliasManager) {
    Objects.requireNonNull(config);
    this.context = Objects.requireNonNull(context);
    this.commenter = Objects.requireNonNull(commenter);
    this.buf = Objects.requireNonNull(buf);
    Objects.requireNonNull(aliasManager);
    support = new BuilderSupport(config, commenter, buf, aliasManager);
  }

  public PreparedSql build() {
    interpret();
    return buf.build(commenter);
  }

  void interpret() {
    select();
    from();
    where();
    groupBy();
    having();
    orderBy();
    limit();
    offset();
    forUpdate();
  }

  private void select() {
    buf.appendSql("select ");

    if (context.distinct == DistinctOption.ENABLED) {
      buf.appendSql("distinct ");
    }

    List<PropertyDef<?>> propertyDefs = context.allPropertyDefs();
    if (propertyDefs.isEmpty()) {
      buf.appendSql("*");
    } else {
      for (PropertyDef<?> propertyDef : propertyDefs) {
        column(propertyDef);
        buf.appendSql(", ");
      }
      buf.cutBackSql(2);
    }
  }

  private void from() {
    buf.appendSql(" from ");
    table(context.entityDef);
    if (!context.joins.isEmpty()) {
      for (Join join : context.joins) {
        if (join.kind == JoinKind.INNER) {
          buf.appendSql(" inner join ");
        } else if (join.kind == JoinKind.LEFT) {
          buf.appendSql(" left outer join ");
        }
        table(join.entityDef);
        if (!join.on.isEmpty()) {
          buf.appendSql(" on (");
          int index = 0;
          for (Criterion criterion : join.on) {
            visitCriterion(index, criterion);
            index++;
            buf.appendSql(" and ");
          }
          buf.cutBackSql(5);
          buf.appendSql(")");
        }
      }
    }
  }

  private void where() {
    if (!context.where.isEmpty()) {
      buf.appendSql(" where ");
      int index = 0;
      for (Criterion criterion : context.where) {
        visitCriterion(index++, criterion);
        buf.appendSql(" and ");
      }
      buf.cutBackSql(5);
    }
  }

  private void groupBy() {
    if (context.groupBy.isEmpty()) {
      List<PropertyDef<?>> propertyDefs = context.allPropertyDefs();
      if (propertyDefs.stream().anyMatch(p -> p instanceof AggregateFunction<?>)) {
        List<PropertyDef<?>> groupKeys =
            propertyDefs.stream()
                .filter(p -> !(p instanceof AggregateFunction<?>))
                .collect(toList());
        context.groupBy.addAll(groupKeys);
      }
    }
    if (!context.groupBy.isEmpty()) {
      buf.appendSql(" group by ");
      for (PropertyDef<?> p : context.groupBy) {
        column(p);
        buf.appendSql(", ");
      }
      buf.cutBackSql(2);
    }
  }

  private void having() {
    if (!context.having.isEmpty()) {
      buf.appendSql(" having ");
      int index = 0;
      for (Criterion criterion : context.having) {
        visitCriterion(index++, criterion);
        buf.appendSql(" and ");
      }
      buf.cutBackSql(5);
    }
  }

  private void orderBy() {
    if (!context.orderBy.isEmpty()) {
      buf.appendSql(" order by ");
      for (Pair<OrderByItem, String> pair : context.orderBy) {
        pair.fst.accept(
            new OrderByItem.Visitor() {

              @Override
              public void visit(OrderByItem.Name name) {
                column(name.value);
              }

              @Override
              public void visit(OrderByItem.Index index) {
                buf.appendSql(String.valueOf(index.value));
              }
            });
        buf.appendSql(" " + pair.snd + ", ");
      }
      buf.cutBackSql(2);
    }
  }

  private void limit() {
    if (context.limit != null) {
      buf.appendSql(" limit ");
      buf.appendSql(context.limit.toString());
    }
  }

  private void offset() {
    if (context.offset != null) {
      buf.appendSql(" offset ");
      buf.appendSql(context.offset.toString());
    }
  }

  private void forUpdate() {
    if (context.forUpdate != null) {
      ForUpdateOption option = context.forUpdate.option;
      if (option != ForUpdateOption.DISABLED) {
        buf.appendSql(" for update");
        if (option == ForUpdateOption.NOWAIT) {
          buf.appendSql(" nowait");
        }
      }
    }
  }

  private void table(EntityDef<?> entityDef) {
    support.table(entityDef);
  }

  private void column(PropertyDef<?> propertyDef) {
    support.column(propertyDef);
  }

  private void visitCriterion(int index, Criterion criterion) {
    support.visitCriterion(index, criterion);
  }
}