# EXCERCISE 1 Confluent Kafka Cloud/Login

Create an account into Confluent Kafka Cloud.

## Confluent Cloud

**Steps:**

1. Create a Confluent Cloud account. You can use the free trial, which will be enough for this excercise. You can create an account [here](https://confluent.cloud/signup). We will use the Sign Up with Google option, but you can choose whichever you prefer.
   ![images/imgage1.png](images/image1.png)

2. Provide your full name and as a company write "EDEM" or "GFT". Click on Next.
 ![images/imgage1.png](images/image1.png)

3. In the next screen, select the options that describe your expertise with Kafka best. Click on Next.
   ![images/imgage3.png](images/image3.png)

4. Now we can configure the cluster that will be used for this excercise. Choose Google Cloud as the cloud provided and Madrid as the region. Leave the name as "cluster_0". Click on Next.
   ![images/imgage4.png](images/image4.png)

5. The next screen is for configuring the billing. However, you **don't need to include any payment information**. Just click on Skip at the bottom left of the screen.
   ![images/imgage5.png](images/image5.png)

6. Now the environment is created and you can click on "keep exploring on my own".
   ![images/imgage6.png](images/image6.png)

7. We will now create the topic to which we will send the messages. Click on the "Topics" tab on the left side of the screen and then click on "Create Topic".
   ![images/imgage7.png](images/image7.png)

8. Change the Topic name to "topic_java" and the Partitions to 1. Click on Create with defaults.
   ![images/imgage8.png](images/image8.png)

9. Now the topic has been created. Confluent allows now to define a schema for the messages, but for this excercise we will not use it. Click on Skip for now.
   ![images/imgage9.png](images/image9.png)