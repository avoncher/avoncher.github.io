set -e
REMOTE_URL="$(git remote get-url origin)"
NAME="$(git config user.name)"
EMAIL="$(git config user.email)"
cd output
trap 'rm -rf .git' EXIT
git init -q
git checkout -q -b gh-pages
git add -A
git -c user.name="$NAME" -c user.email="$EMAIL" commit -q -m "Deploy"
git push -f "$REMOTE_URL" gh-pages
