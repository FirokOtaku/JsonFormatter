# Json Formatter

技术验证型项目, 测试 Native Image 打包功能, 顺便给自己写一个脱离于 VSCode 等编辑器和 IDE 的 JSON 格式化工具用.

### 用法

```bash
# 不加参数或者加 -f 参数的话是格式化 JSON
jf.exe a.json b.json ...
jf.exe -f a.json b.json ...
# 加 -c 参数的话是压缩 JSON
jf.exe -c a.json b.json ...
# 其实, 直接在资源管理器里把文件拖到 .exe 上也行

# 查看版本号等信息
jf.exe -v
```

### 已知问题

文件路径包含中文等非 ASCII 字符时会 ~~爆炸~~ 找不到文件.

### 自行编译

```bash
# 先打出 jar 包
mvn package
# 需要在 Visual Studio 提供的 Native Tools Command Prompt 中执行
native-image -H:-CheckToolchain --link-at-build-time --no-fallback -march=x86-64-v3 -jar jf-{VERSION}-jar-with-dependencies.jar
```
