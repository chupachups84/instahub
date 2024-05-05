import {FETCH_FOLLOWS_FAILED, FETCH_FOLLOWS_SUCCEEDED} from "../actions/subscriptionActionsTypes";

const follows = JSON.parse(localStorage.getItem("followers"));

const initialState = follows
    ? {
        isLoading: true,
        follows
    }
    : {
        isLoading: false,
        follows: null
    };

export default function (state = initialState, action) {

    const { type, payload } = action;

    switch (type) {
        case FETCH_FOLLOWS_SUCCEEDED:
            return {
                ...state,
                isLoading: true,
                follows: payload.follows,
            };
        case FETCH_FOLLOWS_FAILED:
            return {
                ...state,
                isLoading: false,
                follows: null,
            };
        default:
            return state;
    }
}