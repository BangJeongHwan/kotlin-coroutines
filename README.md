# kotlin-coroutines
## 설명
kotlin-coroutines 책에서 배운 내용을 정리한다.

## init setting
### (설정필수)git commit hook 설정
git 커밋시 ktlintCheck를 수행한다.
```bash
$ cd $PROJECT_DIR
$ ./gradlew addKtlintCheckGitPreCommitHook
```