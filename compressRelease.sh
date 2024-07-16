#!/bin/bash

cd app/prod/release || exit
tar czvf ../release_prod.tar.gz -- *
cd - || exit

cd app/demo/release || exit
tar czvf ../release_demo.tar.gz -- *
cd - || exit
