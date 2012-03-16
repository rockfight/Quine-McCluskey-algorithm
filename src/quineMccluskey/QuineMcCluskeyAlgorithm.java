package quineMccluskey;



import java.util.*;
import java.io.*;
import java.lang.*;

public class QuineMcCluskeyAlgorithm {

    //array to store the minterms in each pass
    static String[] minterms = new String[200];
    static String[] dontcares = new String[200];
    static String check_list[] = new String[200];
    
    
    static String[] pass_dontcare = new String[200];
    static String[][] track = new String[100][100];
    static String[][] track2 = new String[100][100];
    static String[][] prime_calc = new String[100][100];
    //array to store the minterms that is already covered
    
    //array to store the input minterms according to the number of 1 bits
    static String[][] group_wise = new String[100][100];
    //to keep track of backward minterms
    static String[][] list_track = new String[200][3];
    //to keep track of completed variables
    static String[] done = new String[200];
    static String[] undone = new String[200];
    //to track the done[]'s index to store completed variables
    static int term = 0;
    static String[] sub_minterms = new String[200];
    static int sub_min_index = 0;
    //index for main array
    static int check_list_index;
    //to check if process completed
    static char complete_flag = 'F';
    static char first_pass_flag = 'T';
    //to check if this is the essential minterms
    static int essential_flag = 0;
    static char first_prime_flag = 'T';
    static char stop_flag='F';
    //temp variable to input the minterms
    static String[] passed_minterms = new String[200];
    static String[] on_hold_minterms = new String[200];
    static int passed_index = 0;
    static int on_hold_index = 0;
    //to keep track of tracking array's row and column
    static int track_row = 0;
    static int track_col = 0;
    // static int max_minterm;
    // static int number_of_bits = 0;
    //static int combined_row = -1;
    //static int combined_column = -1;
    //static int number_of_variables_required;
    static int[] temp_array;
    // static String [] minterms;
    static String temp_min2;
    static int bits = 0;
    static int pass_index = 0;
    static String[] first_list = new String[100];
    static int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0;

    public static void main(String[] args) {
        char if_dontcares;
        int[] int_minterms = new int[200];
        
        int[] int_dontcares = new int[200];
        
        Scanner s = new Scanner(System.in);
        
       while(stop_flag=='F'){
        int_minterms = fill_1d_int_array(int_minterms);
        int_dontcares = fill_1d_int_array(int_dontcares);
        System.out.println("Enter the minterms seperated by return key and enter -1 when completed");
        int_minterms = scan_minterms();

        
        System.out.println("Are there don't care conditions (Y/N)");
        if_dontcares = s.next().charAt(0);
        if (if_dontcares == 'y' || if_dontcares == 'Y') {
            System.out.println("Enter the dont cares seperated by return key and enter -1 when completed");
            int_dontcares = scan_minterms();
            
            
            
        } else;

        bits = get_bits(int_minterms);
       }
        minterms = to_binary(int_minterms);
       
        
        dontcares = to_binary(int_dontcares);
      
        

        complete_methods();
    }

    public static int[] scan_minterms() {
        
        int a = 0, b = 0;
        Scanner scan = new Scanner(System.in);

        int[] temp_array = new int[200];
        temp_array = fill_1d_int_array(temp_array);
        a = 0;

        int input_minterms;
        
        while ((input_minterms = scan.nextInt()) != -1) {
            temp_array[b] = input_minterms;
            b++;
        }

        return temp_array;

    }

    public static int get_bits(int[] min_array) {
        int max = 0;
        int a = 0;
        int bits = 0;
        while (min_array[a] != -1) {
            if (min_array[a] > max) {
                max = min_array[a];
            }
            a++;
        }
        if (max == 0) {
            bits = 0;
            stop_flag='C';
        } else if (max == 1) {
            bits = 1;
            stop_flag='C';
        } else if (max > 1 && max < 4) {
            bits = 2;
            stop_flag='C';
        } else if (max >= 4 && max < 8) {
            bits = 3;
            stop_flag='C';
        } else if (max >= 8 && max < 16) {
            bits = 4;
            stop_flag='C';
        } else if (max >= 16 && max < 32) {
            bits = 5;
            stop_flag='C';
        } else if (max >= 32 && max < 64) {
            bits = 6;
            stop_flag='C';
        } else if (max >= 65 && max <128){
            bits = 7;
            stop_flag='C';
        } else if(max >=128 && max<256){
            bits=8;
            stop_flag='C';
        } else if(max>=256 && max<512){
            bits=9;
            stop_flag='C';
        }
        else if (max>=512 && max<1024){
            bits=10;
            stop_flag='C';
        }
        else if(max>=1024 && max<2048){
            bits=11;
            stop_flag='C';
        }
        else if (max>=2048 && max<4096){
            bits=12;
            stop_flag='C';
        }
        else if (max>2048 && max<8192){
            bits=13;
            stop_flag='C';
        }
        else {
            System.out.println("Only process upto the minterm less than 8191");
            stop_flag = 'F';
        }
        return bits;
    }

    public static String[] to_binary(int[] min_array) {
        int a = 0;
        String return_minterms[] = new String[200];
        return_minterms = fill_1d_array(return_minterms);

        while (min_array[a] != -1) {
            return_minterms[a] = to_binary_string(min_array[a]);
            a++;
        }
        return return_minterms;
    }

    public static void complete_methods() {
        int a = 0, b = 0;
        String[][] final_pass = new String[100][100];
        
        String[][] remaining_minterms = new String[100][100];
        remaining_minterms = fill_arrays(remaining_minterms);
        
        final_pass = fill_arrays(final_pass);         
        prime_calc = fill_arrays(prime_calc);
        pass_dontcare = fill_1d_array(pass_dontcare);
        sub_minterms = fill_1d_array(sub_minterms);
        
        
        
        final_pass = fill_minterms(minterms);
        track = final_pass;
        while (minterms[a] != "-1") {
            a++;
        }



        while (dontcares[b] != "-1") {
            minterms[a] = dontcares[b];
            a++;
            b++;
        }

        int chek = 0;
        a = 0;
        b = 0;
        
        final_pass = fill_minterms(minterms);


       for (int i = 0; i < final_pass.length; i++) {
            for (int j = 0; j < final_pass[i].length; j++) {
                if (final_pass[i][j] != "-1") {
                    prime_calc[b][0] = final_pass[i][j];
                    b++;
                }
            }

        }

       


        pass_dontcare = fill_dontcare(dontcares);









        while(complete_flag != 'T'){
            final_pass=compare_adjacent_minterms(final_pass);
        }
        
         
        
        

     



        remove_duplicate_primes();
        remaining_minterms = get_essential_primes();
        if (remaining_minterms[0][0] != "-1") {
            remaining_minterms = get_primes(remaining_minterms);
        }
        
        a=0;b=0;
        
        
        while(prime_calc[a][0]!="-1" && prime_calc[a][0] != "D" && prime_calc[a][0] != "Y"){
            sub_minterms[sub_min_index]=prime_calc[a][0];
            sub_min_index++;
            a++;
        }
       
        

        give_output(sub_minterms);




    }

    public static String[][] fill_minterms(String[] input_min) {
        String temp_min;
        int a = 0, count;
        int ones = 0, twos = 0, threes = 0, fours = 0, fives=0, sixes=0, sevens=0,eights=0,nines=0,tens=0,elevens=0,twelves=0,thirteens=0;
        String[][] group_wise = new String[100][100];
        fill_arrays(group_wise);



        while (input_min[a] != "-1") {

            count = 0;
            temp_min = input_min[a];


            while (temp_min.length() < bits) {
                temp_min = "0" + temp_min;
            }


            for (int i = 0; i < temp_min.length(); i++) {
                if (temp_min.charAt(i) == '1') {
                    count++;
                }
            }

            if (count == 0) {
                group_wise[0][0] = temp_min;
            } 
            else if (count == 1) {
               
                group_wise[1][ones] = temp_min;
                ones++;
            } 
            else if (count == 2) {
                
                group_wise[2][twos] = temp_min;
                twos++;
            } 
            else if (count == 3) {
                
                group_wise[3][threes] = temp_min;
                threes++;
            } 
            else if (count == 4) {
                
                group_wise[4][fours] = temp_min;
                fours++;
            } 
            else if (count == 5) {
               
                group_wise[5][fives] = temp_min;
                fives++;
            }
            else if (count == 6) {
               
                group_wise[6][sixes] = temp_min;
                sixes++;
            }
            
           else if (count == 7) {
               
                group_wise[7][sevens] = temp_min;
                sevens++;
           }
            else if (count == 8) {
               
                group_wise[8][eights] = temp_min;
                eights++;
           }
            else if (count == 9) {
               
                group_wise[9][nines] = temp_min;
                nines++;
           }
            else if (count == 10) {
               
                group_wise[10][tens] = temp_min;
                tens++;
           }
            else if (count == 11) {
               
                group_wise[11][elevens] = temp_min;
                elevens++;
           }
            else if (count == 12) {
               
                group_wise[12][twelves] = temp_min;
                twelves++;
           }
            else if (count == 13) {
               
                group_wise[12][thirteens] = temp_min;
                thirteens++;
           }
            
            a++;
        }







        return group_wise;







    }

    public static String[] fill_dontcare(String[] input_min) {
        String[] group_wise_dontcare = new String[200];
        group_wise_dontcare = fill_1d_array(group_wise_dontcare);
        String temp_min;
        int a = 0, count, b = 0;



        while (input_min[a] != "-1") {

            count = 0;
            temp_min = input_min[a];


            while (temp_min.length() < bits) {
                temp_min = "0" + temp_min;
            }

            group_wise_dontcare[b] = temp_min;
            b++;
            a++;
        }







        return group_wise_dontcare;







    }

    public static String[][] compare_adjacent_minterms(String[][] group_wise) {
        String[][] all_combined = new String[100][100];
        int count = 0,c=0;
        int index = 0;
        int min_comp = 0, min_comp2 = 1, min_comp1 = 0, min_comp3 = 0;
        int index1 = 0, index2 = 0;




        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                all_combined[i][j] = "-1";
            }
        }
       while (c<group_wise.length) {
        if(group_wise[c][0]!="-1" && group_wise[c+1][0] != "-1") count++;
         c++;
     }
       
        
        
        

if(count >=1){
    count=0;
        while (min_comp < group_wise.length - 1) {


            while (group_wise[min_comp][min_comp1] != "-1") {



                while (group_wise[min_comp2][min_comp3] != "-1") {






                    for (int ij = 0; ij < bits; ij++) {

                        if (group_wise[min_comp][min_comp1].charAt(ij) != group_wise[min_comp2][min_comp3].charAt(ij)) {
                            count++;
                            index = ij;
                        }
                    }


                    if (count == 1) {

                        all_combined[index1][index2] = group_wise[min_comp2][min_comp3].substring(0, index) + 'x' + group_wise[min_comp2][min_comp3].substring(index + 1, bits);
                       
                        prime_fill(all_combined[index1][index2], group_wise[min_comp][min_comp1], group_wise[min_comp2][min_comp3]);
                        index2++;

                    }

                    count = 0;
                    min_comp3++;


                }

                min_comp3 = 0;

                min_comp1++;




            }

            min_comp++;
            min_comp2++;
            min_comp1 = 0;
           
                index1++;
            
            index2 = 0;
            min_comp3 = 0;

        }
first_prime_flag='F';
}

else complete_flag= 'T';
        return all_combined;
    }

    public static void prime_fill(String result, String op1, String op2) {

        int a = 0, b = 0, j = 1;
        if (first_prime_flag == 'T') {
            for (int i = 0; i < prime_calc.length; i++) {

                if (prime_calc[i][0] == op1 || prime_calc[i][0] == op2) {

                    while (prime_calc[i][j] != "-1") {
                        j++;
                    }
                    prime_calc[i][j] = result;
                    j = 1;
                }


            }
        } 
        else {


            for (int m = 0; m < prime_calc.length; m++) {
                for (int n = 0; n < prime_calc[m].length; n++) {
                    if (prime_calc[m][n] == op1 || prime_calc[m][n] == op2) {

                        prime_calc[m][n] = result;

                    }
                }

            }

        }
    }

    public static void remove_duplicate_primes() {
        int prime_calc_index = 0, current_index = 1, check_index = 2, temp_check_index = 0;
        int row_index = 0, dontcare_index = 0;
        String checking_term;
        





        while (prime_calc[prime_calc_index][0] != "-1") {
            checking_term = prime_calc[prime_calc_index][current_index];

            while (prime_calc[prime_calc_index][current_index] != "-1") {
                check_index = current_index + 1;
                while (prime_calc[prime_calc_index][check_index] != "-1") {
                    temp_check_index = check_index;



                    if (prime_calc[prime_calc_index][current_index].equals(prime_calc[prime_calc_index][check_index])) {


                        while (prime_calc[prime_calc_index][temp_check_index] != "-1") {
                            prime_calc[prime_calc_index][temp_check_index] = prime_calc[prime_calc_index][temp_check_index + 1];
                            temp_check_index++;
                        }


                        prime_calc[prime_calc_index][temp_check_index] = "-1";
                        check_index--;
                    }



                    check_index++;
                }
                current_index++;
            }


            current_index = 1;
            check_index = 2;
            prime_calc_index++;
        }

        
    }

    public static String[][] get_essential_primes() {
        int i = 0, j = 0, count = 0;
        int a = 0, b = 0;
        sub_min_index = 0;
        String[] temp_back = new String[100];
        temp_back = fill_1d_array(temp_back);
        int temp_back_index = 0;
        int k = 0;

        String[][] temp_prime_calc = new String[100][100];
        temp_prime_calc = fill_arrays(temp_prime_calc);

        
        

        while (prime_calc[i][0] != "-1") {
            while (pass_dontcare[j] != "-1") {
                if (prime_calc[i][0].equals(pass_dontcare[j])) {
                    prime_calc[i][0] = "D";
                }
                j++;
            }
            j = 0;
            i++;
        }



        i = 0;
        j = 0;
        while (prime_calc[i][0] != "-1") {
            if (prime_calc[i][2].equals("-1") && prime_calc[i][0] != "D" && prime_calc[i][1] !="-1") {
                sub_minterms[sub_min_index] = prime_calc[i][1];
                sub_min_index++;

                while (prime_calc[a][0] != "-1") {
                    while (prime_calc[a][b] != "-1") {
                        if (prime_calc[a][b].equals(prime_calc[i][1])) {
                            prime_calc[a][0] = "Y";
                        }

                        b++;
                    }
                    b = 0;
                    a++;
                }
                a = 0;
                b = 0;

            }
            i++;
        }


        

        

        a = 0;
        b = 0;
        int c = 0, d = 0;
        while (prime_calc[a][0] != "-1") {
            if (prime_calc[a][0] != "D" && prime_calc[a][0] != "Y") {
                while (prime_calc[a][b] != "-1") {
                    temp_prime_calc[c][d] = prime_calc[a][b];
                    b++;
                    d++;


                }
                c++;
            }
            a++;

            b = 0;
            d = 0;
        }


        return temp_prime_calc;


    }

    public static String[][] get_primes(String[][] temp_prime_calc) {
        int x = 0, a = 0, b = 1, c = 0, d = 0, i = 0;
        int count = 0;
        String max_minterm;
        int max_index = 0;
        String[] primes = new String[100];
        primes = fill_1d_array(primes);

        int[] prime_count = new int[100];
        prime_count = fill_1d_int_array(prime_count);

        String[][] temp_prime_calc2 = new String[100][100];
        temp_prime_calc2 = fill_arrays(temp_prime_calc2);




        while (temp_prime_calc[a][0] != "-1") {
            while (temp_prime_calc[a][b] != "-1") {
                primes[i] = temp_prime_calc[a][b];
                b++;
                i++;
            }
            a++;
        }
        
       
        a = 0;
        i = 0;
        while (primes[i] != "-1") {
            while (temp_prime_calc[a][0] != "-1") {
                while (temp_prime_calc[a][b] != "-1") {
                    if (primes[i] == temp_prime_calc[a][b]) {
                        count++;
                        b = 1;
                        break;
                    }

                    b++;
                }
                b = 1;
                a++;
            }
            a = 0;
            b = 1;
            i++;
            prime_count[c] = count;
            count = 0;
            c++;
        }

        count = 0;
        c = 0;
        while (prime_count[c] != -1) {
            if (prime_count[c] > count) {
                count = prime_count[c];
                max_index = c;
            }
            c++;
        }
        a = 0;
        b = 1;
        max_minterm = primes[max_index];
        if(max_minterm !="-1"){
        sub_minterms[sub_min_index] = max_minterm;
        sub_min_index++;
        }
        
       

        while (temp_prime_calc[a][0] != "-1") {
            while (temp_prime_calc[a][b] != "-1") {
                if (temp_prime_calc[a][b].equals(max_minterm)) {
                    temp_prime_calc[a][0] = "Y";
                }

                b++;
            }
            b = 0;
            a++;
        }

        a = 0;
        b = 0;
        c = 0;
        d = 0;
        
        while (prime_calc[a][0] != "-1") {
            while (prime_calc[a][b] != "-1") {
                if (prime_calc[a][b].equals(max_minterm)) {
                    prime_calc[a][0] = "Y";
                }

                b++;
            }
            b = 0;
            a++;
        }
        
        a = 0;
        b = 0;
        c = 0;
        d = 0;
        while (temp_prime_calc[a][0] != "-1") {
            if (temp_prime_calc[a][0] != "Y") {
                while (temp_prime_calc[a][b] != "-1") {
                    temp_prime_calc2[c][d] = temp_prime_calc[a][b];
                    b++;
                    d++;


                }
                c++;
            }
            a++;

            b = 0;
            d = 0;
        }

        return temp_prime_calc2;



    }

    public static void give_output(String[] output_array) {

        int temp_pass_index;
        int output_array_index = 0;
        int h = 0;
        int count = 0;
        temp_pass_index = pass_index;
        String[] minimized_array = new String[200];
        fill_1d_array(minimized_array);
        int min_comp = 0, min_comp2 = 0, min_comp1 = 0, min_comp3 = 0;
        int minimized_index = 0;
        String output_string = "";

        

        String temp_minterm = "";

        int sub_min_index = 0;
        String final_output = "";
        String temp_minterm_check = "";
        int i = 0;

        while (output_array[i] != "-1") {
            for (int ij = bits - 1; ij >= 0; ij--) {

                if (output_array[i].charAt(ij) != 'x') {

                    if (output_array[i].charAt(ij) == '0') {
                        if (ij == 0) {
                            temp_minterm_check = "a";
                        }
                        if (ij == 1) {
                            temp_minterm_check = "b";
                        }
                        if (ij == 2) {
                            temp_minterm_check = "c";
                        }
                        if (ij == 3) {
                            temp_minterm_check = "d";
                        }
                        if (ij == 4) {
                            temp_minterm_check = "e";
                        }
                        if (ij == 5) {
                            temp_minterm_check = "f";
                        }
                        if (ij == 6) {
                            temp_minterm_check = "g";
                        }
                        if (ij == 7) {
                            temp_minterm_check = "h";
                        }
                        if (ij == 8) {
                            temp_minterm_check = "i";
                        }
                        if (ij == 9) {
                            temp_minterm_check = "j";
                        }
                        if (ij == 10) {
                            temp_minterm_check = "k";
                        }
                        if (ij == 11) {
                            temp_minterm_check = "l";
                        }
                        if (ij == 12) {
                            temp_minterm_check = "m";
                        }
                        if (ij == 13) {
                            temp_minterm_check = "n";
                        }
                    }
                    if (output_array[i].charAt(ij) == '1') {

                        if (ij == 0) {
                            temp_minterm_check = "A";
                        }
                        if (ij == 1) {
                            temp_minterm_check = "B";
                        }
                        if (ij == 2) {
                            temp_minterm_check = "C";
                        }
                        if (ij == 3) {
                            temp_minterm_check = "D";
                        }
                        if (ij == 4) {
                            temp_minterm_check = "E";
                        }
                        if (ij == 5) {
                            temp_minterm_check = "F";
                        }
                        if (ij == 6) {
                            temp_minterm_check = "G";
                        }
                        if (ij == 7) {
                            temp_minterm_check = "H";
                        }
                        if (ij == 8) {
                            temp_minterm_check = "I";
                        }
                        if (ij == 9) {
                            temp_minterm_check = "J";
                        }
                        if (ij == 10) {
                            temp_minterm_check = "K";
                        }
                        if (ij == 11) {
                            temp_minterm_check = "L";
                        }
                        if (ij == 12) {
                            temp_minterm_check = "M";
                        }
                        if (ij == 13) {
                            temp_minterm_check = "M";
                        }
                        
                        

                    }
                    temp_minterm = temp_minterm_check + temp_minterm;

                }
            }


            i++;


            for (int j = 0; j < minimized_array.length; j++) {

                if (minimized_array[j].equals(temp_minterm)) {
                    count++;
                }
            }

            if (count == 0) {
                minimized_array[minimized_index] = temp_minterm;
                minimized_index++;
            }
            temp_minterm = "";
            count = 0;


        }
        i = 0;


        minimized_index = 0;
        while (minimized_array[minimized_index] != "-1") {
            output_string = output_string + "+" + minimized_array[minimized_index];
            minimized_index++;
        }
        output_string = output_string.substring(1, output_string.length());
        System.out.println("\n" + output_string);
    }

    public static String[][] fill_dontcares(String[][] filling_array) {
        int filling_array_index = 0;
        int final_pass_dontcare_index = 0;
        while (pass_dontcare[final_pass_dontcare_index] != "-1") {
            for (int i = 0; i < filling_array.length; i++) {
                for (int j = 0; j < filling_array[i].length; j++) {
                    if (pass_dontcare[final_pass_dontcare_index].equals(filling_array[i][j])) {
                        filling_array[i][j] = "-1";

                    }
                }
            }
            final_pass_dontcare_index++;
        }
        return filling_array;
    }

    public static String[][] fill_arrays(String[][] all_combined) {
        for (int i = 0; i < all_combined.length; i++) {
            for (int j = 0; j < all_combined[i].length; j++) {
                all_combined[i][j] = "-1";
            }
        }

        return all_combined;
    }

    public static int[][] fill_int_arrays(int[][] all_combined) {
        for (int i = 0; i < all_combined.length; i++) {
            for (int j = 0; j < all_combined[i].length; j++) {
                all_combined[i][j] = -1;
            }
        }

        return all_combined;
    }

    public static String[] fill_1d_array(String[] filling_array) {
        for (int i = 0; i < filling_array.length; i++) {
            filling_array[i] = "-1";
        }
        return filling_array;
    }

    public static int[] fill_1d_int_array(int[] temp_array) {

        for (int i = 0; i < temp_array.length; i++) {
            temp_array[i] = -1;
        }

        return temp_array;
    }
    
    public static String to_binary_string(int integer_val){
        int quotient=integer_val,remainder;
        String result="";
       while(quotient != 0){
           
           remainder=quotient%2;
           quotient=quotient/2;
           result=Integer.toString(remainder)+result;
       }
       return result;
    }
}
