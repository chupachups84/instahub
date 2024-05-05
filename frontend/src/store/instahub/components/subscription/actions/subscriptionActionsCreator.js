import {
    FETCH_FOLLOWERS_FAILED,
    FETCH_FOLLOWERS_SUCCEEDED,
    FETCH_FOLLOWS_FAILED,
    FETCH_FOLLOWS_SUCCEEDED
} from "./subscriptionActionsTypes";
import subscriptionService from "../services/subscriptionService";

export const fetchFollowers = (page,size,username) => (dispatch) => {
    return subscriptionService.fetchFollowers(page,size,username)
        .then(
            (data) => {
                dispatch({
                    type: FETCH_FOLLOWERS_SUCCEEDED,
                    payload: { followers: data },
                });
                return Promise.resolve();
            },
            (error) => {
                dispatch({
                    type: FETCH_FOLLOWERS_FAILED,
                });
                return Promise.reject();
            }
        );
};

export const fetchFollows = (page,size,username) => (dispatch) => {
    return subscriptionService.fetchFollows(page,size,username)
        .then(
            (data) => {
                dispatch({
                    type: FETCH_FOLLOWS_SUCCEEDED,
                    payload: { follows: data },
                });
                return Promise.resolve();
            },
            (error) => {
                dispatch({
                    type: FETCH_FOLLOWS_FAILED,
                });
                return Promise.reject();
            }
        );
};