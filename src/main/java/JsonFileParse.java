import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class JsonFileParse {
    public static void main(String args[]) {
        List<String> list = parseJsonFile();

        if (list.size() != 0)
            fileWriter(list);
    }

    private static List<String> parseJsonFile() {
        List<String> list;
        String personInfoString;
        try {
            list = new ArrayList<>();
            JSONObject personJson = (JSONObject) JSONValue.parseWithException(getJsonFile());
            JSONArray personArrays = (JSONArray) personJson.get("persons");

            for (Object personArray : personArrays) {
                JSONObject personInfo = (JSONObject) personArray;
                personInfoString = personInfo.get("name") + " " + personInfo.get("age") + " " + personInfo.get("city");
                list.add(personInfoString);
            }

            return list;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getJsonFile() {
        StringBuilder stringBuilder = new StringBuilder();
        URLConnection urlConnection;
        InputStreamReader in = null;

        try {
            URL url = new URL("http://main3.mysender.ru/persons.json");
            urlConnection = url.openConnection();

            if (urlConnection != null)
                urlConnection.setReadTimeout(10 * 1000);

            if (urlConnection != null && urlConnection.getInputStream() != null) {
                in = new InputStreamReader(urlConnection.getInputStream(), Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);

                int cp;
                while ((cp = bufferedReader.read()) != -1) {
                    stringBuilder.append((char) cp);
                }
                bufferedReader.close();
            }

            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    private static void fileWriter(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileWriter fileWriter = new FileWriter("src/main/resources/out.txt")){
            for (String s : list) {
                stringBuilder.append(s).append("\n");
            }
            fileWriter.write(stringBuilder.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
