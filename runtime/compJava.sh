# execute AS2SCompiler for Java->C
cd ../StSCompiler/dist
java -jar StSCompiler.jar java c ../../runtime/input ../../runtime/output ../../runtime/Tmaps/Java_CST_eCST.tmap
echo -------------------

# compile and execute Java
cd ../../runtime/input
javac *.java
jar -cf App.jar *.class
java -cp App.jar $1
echo -------------------

read -n 1 -s -r -p "Press any key to continue..."
echo \n
echo -------------------

# compile and execute C
cd ../output/objcode
gcc $1.c -o out
./out
echo -------------------
