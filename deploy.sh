#!/bin/sh

mvn install
[ $? -eq 0 ] && docker build --tag deckerego/docmagui:0.2.1 --tag deckerego/docmagui:latest .
