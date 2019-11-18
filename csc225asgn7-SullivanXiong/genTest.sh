#!//bin/bash

#### Check for the existance of an argument for the sample file.
# if [ $# -eq 0 ]
#     then
#         echo "No sample input file argument supplied. please run './genTest sample.in'."
#         exit 1
# fi

echo -e "~~~~~~~~~~~~~~~~~~~~~  Compiling: gcc -Wall -Werror -ansi -pedantic countDriver.c countFuncs.c\n"
gcc -Wall -Werror -ansi -pedantic countDriver.c countFuncs.c

echo -e "~~~~~~~~~~~~~~~~~~~~~  Sample File: sample1.in, sample2.in, sample3.in, sample4.in, sample5.in"
./a.out < sample1.in > my1.out
./a.out < sample2.in > my2.out
./a.out < sample3.in > my3.out
./a.out < sample4.in > my4.out
./a.out < sample5.in > my5.out

echo -e "~~~~~~~~~~~~~~~~~~~~~  Output File: my1.out, my2.out, my3.out, my4.out, my5.out\n"
echo -e "~~~~~~~~~~~~~~~~~~~~~  Diffing: my*.out & sample*.out"
diff -w my1.out sample1.out > err1.txt
diff -w my2.out sample2.out > err2.txt
diff -w my3.out sample3.out > err3.txt
diff -w my4.out sample4.out > err4.txt
diff -w my5.out sample5.out > err5.txt
echo -e "~~~~~~~~~~~~~~~~~~~~~  Diff Output: err*.txt"

COUNT=0
ERR=1

while [ $COUNT -lt 5 ]
do
    COUNT=$(( $COUNT + 1 ))
    if [ -s "err$COUNT.txt" ]
        then
            ERR=0
            echo -e "\nYou are not passing all the test cases for sample$COUNT. Keep trying!"
            echo "diff: ~~~~~~~~~~~~~~~"
            cat "err$COUNT.txt"
            echo "~~~~~~~~~~~~~~~~~~~~~"
        else
            echo -e "\nNice work you successfully passed all the test cases for sample$COUNT!"
    fi
done

if [ $ERR -eq 0 ]
    then
        echo -e "\n'<' -> Your file."
        echo "'>' -> sample.out file."
fi

#### To convert from windows file to unix/linux.
# In other words. an noeol file to a lf file.
#sed -i -e 's/\r$//' ./genTest.sh"