import {FETCH_FOLLOWERS_FAILED, FETCH_FOLLOWERS_SUCCEEDED} from "../actions/subscriptionActionsTypes";

const followers = JSON.parse(localStorage.getItem("followers"));

const initialState = followers
    ? {
        isLoading: true,
        followers
    }
    : {
        isLoading: false,
        followers: null
    };

export default function (state = initialState, action) {

    const { type, payload } = action;

    switch (type) {
        case FETCH_FOLLOWERS_SUCCEEDED:
            return {
                ...state,
                isLoading: true,
                followers: payload.followers,
            };
        case FETCH_FOLLOWERS_FAILED:
            return {
                ...state,
                isLoading: false,
                followers: null,
            };
        default:
            return state;
    }
}