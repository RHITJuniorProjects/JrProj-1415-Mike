#!/bin/bash

DIR="$( cd "$( dirname "$0" )" && pwd )"

pages=("index" "projects" "milestones" "tasks")

for page in ${pages[@]}; do
	php "$DIR"/templates/"$page".php > "$DIR"/public/"$page".html
	echo "created ${page}.html"
done

firebase deploy
