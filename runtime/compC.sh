# execute AS2SCompiler for C->Java
cd ../StSCompiler/dist
java -jar AS2SCompiler.jar c java ../../runtime/input ../../runtime/output ../../runtime/Tmaps/C_CST_eCST.tmap
echo -------------------

# compile and execute C
cd ../../runtime/input
gcc $1.c -o out
./out
echo -------------------

read -n 1 -s -r -p "Press any key to continue..."
echo \n
echo -------------------

# compile and execute Java
cd ../output/objcode
javac *.java
jar -cf App.jar *.class
java -cp App.jar $1
echo -------------------
