# execute tests: AS2SCompiler for C->Java

oracle="jvm_jdk/oracle/jdk-11.0.7/bin"
open="jvm_jdk/openjdk/jdk-11/bin"
objcode="runtime/output/objcode"
timelimite="2s"

getmuts(){
    # generates all mutants for a given file

    muts=" "

    while IFS= read -r line || [[ -n "$line" ]]; do
        muts+=" -m=${line}"
    done < mutation/c/muts.txt

    obj="${D}/mutants"

    mkdir -p ${obj}
    rm -r ${obj}/*

    ./mutation/c/music -o ${obj} ${fileC} ${muts}
}

compExits(){
    local j_env=$1
    resp=0
    if [ "$C" -eq "$J" ]; then
        if [ "$C" -eq "0" ]; then
            
            timeout $timelimite ./${c_exe} ${line} &> ${objcode}/c_out.txt
            timeout $timelimite ${j_env}/java -cp ${j_exe} Main ${line} &> ${objcode}/j_out.txt
            
            # compare files
            diff ${objcode}/c_out.txt ${objcode}/j_out.txt > ${objcode}/diff_resp.txt
            bytes=`stat --printf="%s" ${objcode}/diff_resp.txt`
            
            if [ "$bytes" -ne "0" ]; then
                resp=1
            fi
        fi
    else
        if [[ "$C" -eq "0" || "$J" -eq "0" ]]; then
            resp=1
        fi
    fi
}

countPoints(){
    if [ "$resp" -eq "0" ]; then
        if [ "$op" == "Gcc_Oracle" ]; then
            Gcc_Oracle=$((Gcc_Oracle + 1))
        fi
        if [ "$op" == "Clang_Oracle" ]; then
            Clang_Oracle=$((Clang_Oracle + 1))
        fi
        if [ "$op" == "Gcc_Open" ]; then
            Gcc_Open=$((Gcc_Open + 1))
        fi
        if [ "$op" == "Clang_Open" ]; then
            Clang_Open=$((Clang_Open + 1))
        fi
    fi
}

magicTest(){
    # auxiliar function of testing
    local j_env=$1
    
    timeout $timelimite ./${c_exe} ${line} &> /dev/null
    C=$?
    
    timeout $timelimite ${j_env}/java -cp ${j_exe} Main ${line} &> /dev/null
    J=$?

    compExits $j_env

    countPoints
}

testfile(){
    # tests a C program (from a prepared folder)

    Daux=$1

    # translation
    message=$( { ${oracle}/java -jar AS2SCompiler.jar c java ${Daux} runtime/output runtime/Tmaps/C_CST_eCST.tmap; } 2>&1 )

    # ignore pragrams that are not translated
    if [[ "$message" =~ ^Object.* ]]; then

        # generate complete C test
        cat ${Daux}/*.c > ${Daux}/test.c && cat ${Daux}/test_part_org.txt >> ${Daux}/test.c

        # compiling C
        gcc ${Daux}/test.c -o ${Daux}/out_gcc
        if [ "$?" -ne "0" ]; then
            return 
        fi        

        clang ${Daux}/test.c -o ${Daux}/out_clang
        if [ "$?" -ne "0" ]; then
            return 
        fi  

        # generate complete Java test
        cat ${Daux}/test_part_obj.txt > ${objcode}/Main.java

        # compiling Java
        cd ${objcode}

        ../../../${oracle}/javac *.java
        if [ "$?" -ne "0" ]; then
            cd ../../../
            return 
        fi  
        ../../../${oracle}/jar -cf App_Oracle.jar *.class   
        if [ "$?" -ne "0" ]; then
            cd ../../../
            return 
        fi  

        ../../../${open}/javac *.java
        if [ "$?" -ne "0" ]; then
            cd ../../../
            return 
        fi  
        ../../../${open}/jar -cf App_Open.jar *.class  
        if [ "$?" -ne "0" ]; then
            cd ../../../
            return 
        fi  

        cd ../../../    

        Gcc_Oracle=0
        Clang_Oracle=0
        Gcc_Open=0
        Clang_Open=0

        counter=0
        while IFS= read -r line || [[ -n "$line" ]]; do

            #Gcc_Oracle
            c_exe="${Daux}/out_gcc"
            j_exe="${objcode}/App_Oracle.jar"            
            op="Gcc_Oracle"
            magicTest $oracle 
            
            # Clang_Oracle
            c_exe="${Daux}/out_clang"
            j_exe="${objcode}/App_Oracle.jar"
            op="Clang_Oracle"
            magicTest $oracle

            #Gcc_Open
            c_exe="${Daux}/out_gcc"
            j_exe="${objcode}/App_Open.jar"
            op="Gcc_Open"
            magicTest $open

            # Clang_Open
            c_exe="${Daux}/out_clang"
            j_exe="${objcode}/App_Open.jar"
            op="Clang_Open"
            magicTest $open

            counter=$((counter + 1))
        done < ${Daux}/test_inputs.txt

        exportsLine="$CURRENT,$counter,,$Gcc_Oracle,$((counter - Gcc_Oracle)),,$Clang_Oracle,$((counter - Clang_Oracle)),,$Gcc_Open,$((counter - Gcc_Open)),,$Clang_Open,$((counter - Clang_Open))"

        echo $exportsLine >> results_C.csv
    fi
}

renameMut(){
    # rename mutant to "root" program's name
    
    local orgname=$(basename $fileC)
    local destname=`find "mutation/c/temp" -maxdepth 1 -type f -name "*.c"`

    mv $destname mutation/c/temp/$orgname
}

printCurrent(){
    # printf current program
    local dirrr=$1
    local actual=`find "$dirrr" -maxdepth 1 -type f -name "*.c"`
    echo "Current Program: $actual"
    CURRENT=$(basename $actual)
}

testEach(){
    # tests each mutant
    local dirrr="mutation/c/temp"
    for M in $D/mutants/*; do
        if [[ $M == *.c ]]; then 
            
            # clean temp folder
            mkdir -p $dirrr
            rm -r $dirrr/*
            
            # copy main files
            cp $M $dirrr
            cp $D/test_part_org.txt $dirrr
            cp $D/test_part_obj.txt $dirrr
            cp $D/test_inputs.txt $dirrr
            
            # show mutant's name
            printCurrent $dirrr

            # rename the mutant
            renameMut

            # test the mutant program
            testfile $dirrr
        fi
    done
}

# main code starts here

for D in runtime/input/c/*; do
    if [ -d "${D}" ]; then
        
        fileC=`find "$D" -maxdepth 1 -type f -name "*.c"`
        
        # get mutants for "root" program
        getmuts

        # prepare result table
        echo "ALL,ALL,,Gcc/Oracle,Gcc/Oracle,,Clang/Oracle,Clang/Oracle,,Gcc/Open,Gcc/Open,,Clang/Open,Clang/Open" > results_C.csv
        echo "ID,Test Cases Number,,Equivalents,Non-equivalent,,Equivalents,Non-equivalent,,Equivalents,Non-equivalent,,Equivalents,Non-equivalent" >> results_C.csv

        # show "root"'s name
        printCurrent $D

        # test the "root" program
        testfile $D

        #clean temps for new tests
        rm ${D}/test.c
        rm ${D}/out_gcc
        rm ${D}/out_clang

        # test each mutant program
        testEach

    fi
done



