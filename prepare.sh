# download all files for testing the compiler

FILEID="15g1e3la4keONvJQl1LC-ibpyOtMlMBzp"
FILENAME="AS2SCompiler.zip"
FOLDER="AS2SCompiler"

# wget files for Google Drive
function gdrive_download () {
  CONFIRM=$(wget --quiet --save-cookies /tmp/cookies.txt --keep-session-cookies --no-check-certificate "https://docs.google.com/uc?export=download&id=$1" -O- | sed -rn 's/.*confirm=([0-9A-Za-z_]+).*/\1\n/p')
  wget --load-cookies /tmp/cookies.txt "https://docs.google.com/uc?export=download&confirm=$CONFIRM&id=$1" -O $2
  rm -rf /tmp/cookies.txt
}

# get mandatory programs
sudo apt-get update
sudo apt-get install unzip clang-format-7 gcc clang

# call function for downloading
gdrive_download $FILEID $FILENAME

# create folders
mkdir -p $FOLDER

# unzip files
unzip $FILENAME -d $FOLDER

# remove original file
rm $FILENAME

# set permissions
chmod -R 777 $FOLDER/*

# convert CRLF files into LF files

sed -i $'s/\r$//' AS2SCompiler/testC.sh AS2SCompiler/testJava.sh AS2SCompiler/mutation/java/major/bin/javac

shopt -s globstar
FILES=" "
for M in AS2SCompiler/mutation/**/*.txt; do
  FILES+="$M "
done
for M in AS2SCompiler/runtime/**/*.txt; do
  FILES+="$M "
done

sed -i $'s/\r$//' $FILES