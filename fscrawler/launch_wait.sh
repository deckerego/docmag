#!/bin/bash

HOST="$1"
PORT="$2"
RUNNING=1

while [[ $RUNNING -ne 0 ]]; do
  echo "Waiting for $HOST:$PORT..."
  sleep 3
  nc -z "$HOST" "$PORT"
  RUNNING=$?
done

${@:3}
