# Right Commons
Some helper functions, dialogs etc used by multiple Right apps.</br>
Based on <a href="https://github.com/SimpleMobileTools/Simple-Commons">Simple-Commons</a>. Uses <a href="https://github.com/FossifyOrg/commons">Fossify's</a> developments.</br>
For reporting bugs/features that affect multiple apps please use the <a href="https://github.com/Goodwy/Discussion">General Discussion</a> repository.

## 发布(AAR 自动化,方案A·自建发布)

本仓库 fork(`chadhao/Android-App-Commons`)的依赖分发已从 JITPack 改为**自建发布**:

- 每次 `push main`(或手动 `workflow_dispatch`,可指定 ref)触发
  [`.github/workflows/publish-commons.yml`](.github/workflows/publish-commons.yml) 自动构建:
  - `:commons:assembleFossRelease` → `commons-foss-<短SHA>.aar`
  - `:strings:assembleRelease` → `strings-<短SHA>.aar`
  - 同时产出 `checksums-<短SHA>.sha256`(sha256sum 校验)
- 产物发布为 GitHub Release,`tag = commons-aar-<短SHA>`(例 `commons-aar-25489aa2`),
  仓库 `chadhao/Android-App-Commons`,公开仓库匿名可下载、一一对应可追溯。
- Phone 仓消费方式: 下载两个 AAR 放入 `app/libs/`,并在 `app/build.gradle.kts` 用
  `implementation(files("libs/commons-foss-<SHA>.aar", "libs/strings-<SHA>.aar"))`
  替代旧 JITPack 坐标,同时显式声明传递依赖(详见 Phone 仓 `app/libs/VERSIONS.md`)。
- 失败可见性: workflow 任一步失败都会上传构建日志 artifact;
  Release 创建失败时显式报错(常见原因: 仓库 Actions 权限非 Read and write)。

> `jitpack.yml` 保留(不再被 chadhao 坐标消费,不删除,避免破坏 fork 语义);
> 本 workflow 不 push 任何 maven 仓库、不依赖 JITPack。
