# add-aws-deps

CLI for adding aws libraries from [cognitect/aws-api](https://github.com/cognitect-labs/aws-api) to your project.

## Requirements

- [fzf](https://github.com/junegunn/fzf)
- [babashka](https://github.com/babashka/babashka)

## Usage

### Install

TODO

### Example usage

Adding to `deps.edn` (default)

```bash
$ add-aws-deps
# user selects N dependencies using fzf
adding 3 dependencies to: deps.edn
+ com.cognitect.aws/lambda #:mvn{:version 847.2.1398.0}
+ com.cognitect.aws/api #:mvn{:version 0.8.686}
+ com.cognitect.aws/endpoints #:mvn{:version 1.1.12.489}
```

Adding to custom file (here, `bb.edn`)

```bash
$ add-aws-deps bb.edn
# user selects N dependencies using fzf
adding 1 dependencies to: bb.edn
+ com.cognitect.aws/iot #:mvn{:version 847.2.1398.0}
```
