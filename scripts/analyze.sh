#!/bin/bash

set -e

echo generating tree
sbt dependencyTree | cut -c 32- > scratch/tree.out
echo generating evicted list
sbt evicted | cut -c 32- > scratch/evicted.out
echo done
