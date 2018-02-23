#!/bin/sh

mvn install
[ $? -eq 0 ] && docker build --tag deckerego/docmagui:0.3.2 --tag deckerego/docmagui:latest .
