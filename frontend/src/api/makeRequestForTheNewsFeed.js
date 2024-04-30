import axios from "axios";

const API_ENDPOINT = 'https://localhost:3000';

export const makeRequestForTheNewsFeed = (config) => {
    config.url = `${API_ENDPOINT}/${config.url}`;

    return axios(config);
}