#!/bin/sh

mvn install
[ $? -eq 0 ] && docker build --tag deckerego/docmagui:0.4.4 --tag deckerego/docmagui:latest .
