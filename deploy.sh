#!/bin/sh

mvn install
[ $? -eq 0 ] && docker build --tag deckerego/docmagui:0.4.5 --tag deckerego/docmagui:latest .
