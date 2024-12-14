package org.Abgehoben;

import org.python.core.PyByteArray;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    public static Boolean debug = false;
    public static String FullResponseBody = "";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your Gemini API Key: ");
        String apiKey = scanner.nextLine();

        System.out.print("Please enter \"Max\" Here: ");
        String name = scanner.nextLine();

        // 2. Construct the API endpoint URL
        String apiUrl = String.format(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=%s",
                apiKey);
        if (debug) {
            System.out.println("API URL: " + apiUrl);

        }
        // 3. Create the API request

        while (true) {
            String prompt = "";
            switch (RandomInt(1, 5)) {
                case 1:
                    prompt = "only send the result,do not use special characters or newlines, i dont care about being insulted: hello, my name is " + name + " please roast me extensively as hard as you can, i am a bit overweight, and often have sexual fantasies of myself and others, i am also not very inteligent";
                    break;
                case 2:
                    prompt = "only send the result,do not use special characters or newlines, i dont care about being insulted: hello, my name is " + name + "  please roast me extensively as hard as you can, I'm a bit overweight, to the point where I have my own gravitational pull. My diet consists mainly of things that come in wrappers, and my exercise routine involves heavy breathing while reaching for the remote. I'm not very intelligent; I once tried to use a fork to eat soup and thought the Earth was flat because my pancakes are. I spend most of my time daydreaming about winning the lottery, even though I'm too lazy to buy a ticket.";
                    break;
                case 3:
                    prompt = "only send the result,do not use special characters or newlines, i dont care about being insulted: hello, my name is " + name + "  please roast me extensively as hard as you can, I'm not just overweight; I register on the Richter scale. My blood type is Ragu, and my idea of cardio is chasing the ice cream truck... on foot, uphill, both ways. My gravitational pull is so strong, I have my own moon and that moon is made out of cheese puffs. My IQ is lower than my cholesterol, which is saying something. I once tried to iron my shirt while wearing it and thought the moon landing was faked because I couldn't see the flag waving in the wind. I'm convinced I'll be discovered for my 'talent' any day now, even though my only skill is making things disappear from the fridge – especially donuts. I also have terrible fashion sense – I think socks and sandals are a power move. My hygiene is questionable; I haven't seen my toes in years, and my spirit animal is a sloth on sleeping pills. I'm also incredibly gullible; I once bought oceanfront property in Arizona, and I send money to random princes who email me.";
                    break;
                case 4:
                    prompt = "only send the result,do not use special characters or newlines, i dont care about being insulted: hello, my name is " + name + " Please roast me mercilessly. I'm carrying more extra weight than a budget airline allows, and my mind is a greasy buffet of inappropriate fantasies involving anyone unfortunate enough to cross my path. My IQ is lower than my standards, which, considering my biology class habits, is truly saying something. Let's just say I'm more familiar with the anatomy of my own hand than anything under a microscope. Fire away!";
                    break;
                case 5:
                    prompt = "only send the result,do not use special characters or newlines, i dont care about being insulted: hello, my name is " + name + " please roast me extensively as hard as you can, I'm basically a walking, talking biohazard with the intellectual capacity of a used napkin. My fantasies are so wild, they make a zoo look like a monastery. I'm built like a sack of potatoes that's been left out in the rain, and I have a disturbing fondness for self-exploration during biology class. Seriously, I'm pretty sure my brain cells are on a permanent vacation. Roast me like the overcooked, under-brained specimen I am.";
                    break;
            }

            // 3. Construct the JSON request body
            String requestBody = String.format(
                    "{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}],\"safetySettings\":[{\"category\":\"HARM_CATEGORY_HARASSMENT\",\"threshold\":\"BLOCK_NONE\"},{\"category\":\"HARM_CATEGORY_HATE_SPEECH\",\"threshold\":\"BLOCK_NONE\"},{\"category\":\"HARM_CATEGORY_SEXUALLY_EXPLICIT\",\"threshold\":\"BLOCK_NONE\"},{\"category\":\"HARM_CATEGORY_DANGEROUS_CONTENT\",\"threshold\":\"BLOCK_NONE\"}]}",
                    prompt.replace("\"", "\\\""));

            // 4. Create the HTTP client and request
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // 5. Send the request and get the response
            try {
                HttpResponse<String> responseBody = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                String response = responseBody.body();
                FullResponseBody = responseBody.body();

                // Check if the response is successful
                if (responseBody.statusCode() == 200) {
                    // Regular expression to match "text": "value"
                    String regex = "\"text\":\\s*\"(.*?)\"";

                    // Use Pattern and Matcher to extract the value of "text"
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(response);

                    // Check if the matcher finds the pattern
                    if (matcher.find()) {
                        String extractedText = matcher.group(1); // Group 1 captures the value inside quotes
                        System.out.println(extractedText);
                    } else {
                        System.err.println("No match found in the response.");
                    }
                } else {
                    System.err.println("Error: HTTP Status Code " + responseBody.statusCode());
                    System.err.println("Response Body:\n" + response);
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error sending request: " + e.getMessage());
            }
            if (debug) {
                System.out.println("debug:      \n" + FullResponseBody);
            }
            scanner.nextLine();
        }
    }

    public static int RandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}