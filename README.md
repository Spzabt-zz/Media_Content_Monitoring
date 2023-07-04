In short, the user can create a project by entering the project name, personal API keys, and keywords for content search. After creating the project, the system connects to the Reddit, Twitter, and YouTube APIs in order to collect the necessary data. Then, the system analyzes the text data for sentiment (using Stanford Core NLP dependency), counts mentions, tracks positive and negative sentiment, measures reach throughout time, and generates a word cloud visualization of the most popular words. Additionally, the user can compare several projects, view social media statistics such as total likes and retweet count, and generate a PDF report with these statistics. Users can also view content analysis for each available source. Default registration and authorization features were also implemented. For this project, I utilized Java, Spring Boot, PostgreSQL with Hibernate, FreeMarker (template engine), Chart.js (for creating charts and diagrams), and flying-saucer (for report generation).
