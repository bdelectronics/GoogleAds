package admanager.axis.auth;

import java.io.*;
import java.util.Arrays;

public class Main_class{

    public static int error = 0;
    public static boolean in_or_out = false;
    public static String path = "";
    public static String file ;
    public static String[] result;
    public static void main_main(){

        String file_name = "resources\\ads.properties";
        File f = new File(file_name);
        path = f.getAbsolutePath();
//        file = "C:\\Users\\Ademola.A\\Downloads\\list adwords google.txt";
//        JOptionPane.showMessageDialog(null, "Step 1.1");
        String line;
        String[][] files_lst = new String [12000][14];
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            int first = 0;
            while ((line = bufferedReader.readLine()) != null ){
                String[] columns = line.trim().split(";");
                try {
                    for (int i = 0; i < columns.length; i++) {
                        files_lst[first][i] = columns[i];
                    }
                    first++;
                }
                catch (Exception e){
                    e.getMessage();
                    result = new String [1];
                    result[0] = "There is something wrong with your file";
                }
            }

            String[][] new_list = {} ;
            int counter = 0;
            Boolean d = false;
            for(int i = 0; i<files_lst.length; i++){
                for(int z = 0; z<files_lst[i].length; z++){
                    if(files_lst[i][z] == null){
                        counter = i;
                        d = true;
                        break;
                    }
                }
                if(d){
                    break;
                }
            }
            new_list = new String [counter][14];
            for(int i = 0; i<counter; i++){
                for(int z = 0; z<files_lst[i].length; z++){
                    new_list[i][z] = files_lst[i][z];
                }
            }

//            JOptionPane.showMessageDialog(null, "Step 1.2");
            try {
                result = new String[new_list.length - 1];
            }
            catch (NegativeArraySizeException e){
                result = new String [1];
                result[0] = "There is something wrong with your file";
            }
            for (int i=1; i<new_list.length; i++){
                error = 0;
                if(new_list[i][13].toLowerCase().equals("false")){
                    Get_campaigns.uidasud();
                    Long campaignID = Get_campaigns.main_campaign_id;
                    create_addgroups.create_addgroups_main( new_list[i][0], campaignID, Float.valueOf(new_list[i][1]) );
                    Add_keywords.keywords_main(create_addgroups.AddgroupID, new_list[i][2]);
                    Add_ads.adwords_main(create_addgroups.AddgroupID, new_list[i][3], new_list[i][4], new_list[i][5],
                            new_list[i][6], new_list[i][7], new_list[i][8],
                            new_list[i][9],new_list[i][10],new_list[i][11], new_list[i][12]);
                    new_list[i][13] = String.valueOf(in_or_out);
                    result[i-1] = "Done with no errors";
                    if(error > 0){
                        remove_add_group.remove_add_group_main(create_addgroups.AddgroupID);
                        new_list[i][13] = String.valueOf(in_or_out);
                        result[i-1] = "There were some errors errors";
                    }
                }

                else{
                    result[i-1] = "Content already exist in the database";
                }
            }
//            JOptionPane.showMessageDialog(null, "Step 1.3");
            bufferedReader.close();
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                for(int x=0; x<new_list.length; x++){
                    for(int i=0; i<new_list[x].length; i++){
                        if(i==new_list[x].length-1){
                            bufferedWriter.write(new_list[x][i]);
                        }
                        else{
                            bufferedWriter.write(new_list[x][i]+";");
                        }
                    }
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


}


