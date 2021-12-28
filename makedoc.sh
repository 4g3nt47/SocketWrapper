#!/bin/bash
# Javadoc documentation maker.
# Author: Umar Abdul.

outdir=$1
if [[ "$outdir" == "" ]]
then
  echo "[-] Usage: ./makedoc.sh <outdir>"
else
  javadoc SocketWrapper.java -d $outdir -author -version -noqualifier java.lang:java.io:java.net
fi
