# jdtformatter

Eclipse JDTでソースコードのフォーマットを行うツール

## gitでの利用例

本ツールはJavaで開発されているため，JDK8+が導入済で，`JAVA_HOME`が設定されている必要がある．

Gradleでjdtformatterをビルドする．
ビルド生成物が `build/install` ディレクトリ下に配置される．

```
$ gradlew install
```

生成された `jdtformatter` ディレクトリを `$HOME` 下に配置する．

`$HOME` にプロジェクト標準のスタイルおよび開発者好みのスタイルの設定を、
それぞれ`style-default.xml`，`style.xml`という名前で保存する．

プロジェクト直下に `.gitattributes`ファイルを以下の内容で追加する．

```
*.java filter=format
```

filter属性にformatが指定されたファイルをgitで操作する際に使用するコマンドを指定する．
`--global`のオプションを`--local`に変更すると、リポジトリにのみ設定が適用される．

```
$ git config --global filter.format.clean ~/jdtformatter/bin/jdtformatter style-default.xml
$ git config --global filter.format.smudge ~/jdtformatter/bin/jdtformatter style.xml
```

## Styleファイル
- `style.xml` : Eclipse Pleiades のデフォルトのスタイル設定
- `slf4j-idea.xml` : slf4jのIntelliJIDEAのスタイル設定．slf4jではEclipse用とIDEA用のスタイルの設定ファイルがプロジェクトに格納されているが、2つで異なる設定内容である
