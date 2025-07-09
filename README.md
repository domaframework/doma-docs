# Doma Documentation

> [!IMPORTANT]
> **This documentation has been migrated to the main Doma repository.**
> 
> Please visit https://github.com/domaframework/doma/ for the latest documentation.
> 
> This repository is now archived and no longer maintained.

---

This repository manages the English version of the [Doma](https://github.com/domaframework/doma) documentation.
Localization into Japanese is managed manually.

We welcome contributions to improve both the English and Japanese versions.

[![docs](https://readthedocs.org/projects/doma/badge/?version=latest)](https://doma.readthedocs.io/en/latest/)

## Deployment site

- English: https://doma.readthedocs.io/en
- Japanese: https://doma.readthedocs.io/ja

## To Those Who Can Help Improve English Documentation

Please read the [CONTRIBUTING.md](CONTRIBUTING.md) file.

## To Those Who Can Help with Japanese Translation

日本語翻訳に協力してくれる方は、[Issues](https://github.com/domaframework/doma-docs/issues) または [Discussions](https://github.com/domaframework/doma-docs/discussions) でお気軽にお声がけください。
英語から日本語への翻訳はもちろん、既存の日本語訳の修正や改善も歓迎します。

### 翻訳の手順

1. **新しい翻訳を追加する場合:**
   - `docs` ディレクトリで `sphinx-build -b gettext . _build/gettext` を実行してPOTファイルを生成
   - `docs/locale/ja/LC_MESSAGES/` 内の対応する `.po` ファイルを編集
   - `msgid` の英語テキストに対応する `msgstr` に日本語翻訳を記入

2. **既存の翻訳を修正する場合:**
   - `docs/locale/ja/LC_MESSAGES/` 内の該当する `.po` ファイルを直接編集
   - 修正したい `msgstr` の内容を変更

3. **ファイル構成:**
   - メインページ: `docs/locale/ja/LC_MESSAGES/index.po`
   - 各セクション: `docs/locale/ja/LC_MESSAGES/{セクション名}.po`
   - クエリ関連: `docs/locale/ja/LC_MESSAGES/query/{ファイル名}.po`

4. **翻訳例:**
   ```
   msgid "Getting Started"
   msgstr "はじめに"
   ```

5. **AIツールを活用した翻訳:**
   - [Claude Code](https://claude.ai/code)、[Gemini CLI](https://github.com/google-gemini/gemini-cli) などのコーディングエージェントツールを使って効率的に翻訳できます
   - 例: 「このPOファイルの未翻訳部分を日本語に翻訳してください」と依頼
   - 大量の翻訳作業や一貫性のチェックに特に有効です

日本語翻訳に関する問い合わせは日本語で登録いただいて構いません。

