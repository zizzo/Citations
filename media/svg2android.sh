#!/bin/bash -e
# Transforms a SVG into a PNG for each platform
# Sizes extracted from
# http://developer.android.com/design/style/iconography.html

#USAGE: bash svg2android.sh immagine.svg -w48 /path to Citations/Citations/res
#svg image --> images for any resolution

[ -z $2 ] && echo -e "ERROR: filename and one dimension (-w or -h) is required, for example:\nsvg2png -w48 icon.svg\n" && exit 1;
FILENAME=$2
DEST_FILENAME=`echo $2 | sed s/\.svg/\.png/`
FLAG=`echo $1 | cut -c1-2`
ORIGINAL_VALUE=`echo $1 | cut -c3-`

if [ "$FLAG" != "-w" ] && [ "$FLAG" != "-h" ]; then
    echo "Unknown parameter: $FLAG" 
    exit 1
fi

# PARAMETERS: {multiplier} {destination folder}
function export {
  VALUE=$(echo "scale=0; $ORIGINAL_VALUE*$1" | bc -l)
  CMD="inkscape $FLAG$VALUE --export-background-opacity=0 --export-png=$3/$2/$DEST_FILENAME $FILENAME > /dev/null"
  echo $CMD
  eval $CMD
} 

export 1 drawable-mdpi $3
export 1.5 drawable-hdpi $3
export 2 drawable-xhdpi $3
export 3 drawable-xxhdpi $3
export 4 drawable-xxxhdpi $3
