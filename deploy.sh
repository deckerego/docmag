#!/bin/sh

mvn install
[ $? -eq 0 ] && docker build --tag deckerego/docmagui:0.1.0 --tag deckerego/docmagui:latest .
