### Spring Boot 3.0 Security with JWT Implementation

![image](https://github.com/manhleodz/Spring_Security_JWT/assets/107250543/7bfe8b86-82ab-432c-89fc-2460f2382694)

## Description:
- This is a small implementation when I started learning and doing a personal project about banking system using java spring boot.
- Use refresh token and access token for identification and authorization
- Users can only log in and use on a single device to ensure safety because it is a banking system. When creating an account successfully, the user logs back in and will create a refresh and access token to save in the database and send to the user with an expiration time of 20 minutes.
- After the token expires, users need to use the refreshToken API to send to refreshToken to authenticate and create a new token. If refreshToken and accessToken match the data in the database, a new refresh and access token will be created. If the user continues to use it, status 403 will be returned.
