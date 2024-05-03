import {combineReducers} from "redux";
import authenticationReducer from "./authentication/reducers/authenticationReducer";
import userReducer from "./users/reducers/userReducer";
import profileReducer from "./profile/reducers/profileReducer";

export const rootReducer = combineReducers({
    authenticationReducer,
    userReducer,
    profileReducer,
});
