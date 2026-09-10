#!/usr/bin/env bash
# verify-aar-inputs.sh — 校验发布产物(AAR/校验和)存在、非空、命名规范、zip 魔数。
#
# 用途: publish-commons.yml 在收集产物后调用,防止把空文件/命名错误/损坏产物发布成 Release。
#
# 用法:
#   bash .github/scripts/verify-aar-inputs.sh <短SHA> <dist目录>
# 例 :
#   bash .github/scripts/verify-aar-inputs.sh 25489aa2 dist

set -euo pipefail

SHA="${1:?usage: verify-aar-inputs.sh <short-sha> <dist-dir>}"
DIST="${2:?usage: verify-aar-inputs.sh <short-sha> <dist-dir>}"

fail() {
  echo "::error::verify-aar-inputs: $*" >&2
  exit 1
}

[ -d "$DIST" ] || fail "dist dir missing: $DIST"

COMMONS_AAR="$DIST/commons-foss-$SHA.aar"
STRINGS_AAR="$DIST/strings-$SHA.aar"
CHECKSUMS="$DIST/checksums-$SHA.sha256"

# 1) 存在性
[ -f "$COMMONS_AAR" ] || fail "missing $COMMONS_AAR"
[ -f "$STRINGS_AAR" ] || fail "missing $STRINGS_AAR"
[ -f "$CHECKSUMS" ]   || fail "missing $CHECKSUMS"

# 2) 非空(AAR 是 zip,最小也 >1KB;空/极小文件视为构建失败产物)
for f in "$COMMONS_AAR" "$STRINGS_AAR"; do
  size="$(stat -c%s "$f" 2>/dev/null || echo 0)"
  [ "$size" -gt 1024 ] || fail "suspiciously small/empty artifact: $f ($size bytes)"
done

# 3) zip 魔数抽查(PK\x03\x04 -> hex 504b0304;这里已足够拦截纯文本/损坏文件)
for f in "$COMMONS_AAR" "$STRINGS_AAR"; do
  magic="$(head -c 2 "$f" | od -An -tx1 | tr -d ' \n')"
  [ "$magic" = "504b" ] || fail "not a zip/aar (bad magic $magic): $f"
done

# 4) 校验和文件内容覆盖两个 AAR
grep -q "commons-foss-$SHA.aar" "$CHECKSUMS" || fail "checksum file missing commons-foss entry"
grep -q "strings-$SHA.aar"       "$CHECKSUMS" || fail "checksum file missing strings entry"

echo "verify-aar-inputs: OK — $COMMONS_AAR, $STRINGS_AAR, $CHECKSUMS"