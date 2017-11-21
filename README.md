# Style Coordinator

This tool coordinate the difference of coding style of each developer.

[Source Code](style-coordinator-master.zip)

## Build & Install

This tool requires Java8+ and environment variable `JAVA_HOME`.

After running the following command, the distribution directory is generated in `build/install`.

```shell
$ gradlew install
```

To install this tool, move generated `style-coordinator` directory under `$HOME`.

## How to use in git

Put style files in `$HOME`.
File describing project standard style is named `style-default.xml`.
File describing developer's favorite style is named `style.xml`.

Add `.gitattribute` in target project.
The following code block shows content of the file.

```shell
*.java filter=format
```

Run the following command.
If you specify an option `--global` alternative to `--local`, this configuration is available only under your repository.

```shell
$ git config --global filter.format.clean ~/style-coordinate/bin/style-coordinater style-default.xml
$ git config --global filter.format.smudge ~/style-coordinator/bin/style-coordinater style.xml
```

## File describing style
The file defined an open standard XML schema for describing coding style.
You can export from Eclipse.
