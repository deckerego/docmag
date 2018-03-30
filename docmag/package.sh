#!/bin/sh

HELM_CHART_DIR="../../helm_charts"

helm dependency update
helm package -d "$HELM_CHART_DIR" .
helm repo index "$HELM_CHART_DIR"
