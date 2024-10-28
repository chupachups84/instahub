import {combineReducers} from 'redux';
import {configureStore} from "@reduxjs/toolkit";
import {FLUSH, PAUSE, PERSIST, persistReducer, persistStore, PURGE, REGISTER, REHYDRATE,} from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import authenticationReducer from "./components/authentication/reducers/authenticationReducer";
import userReducer from "./components/users/reducers/userReducer";
import profileReducer from "./components/profile/reducers/profileReducer";
import followsReducer from "./components/subscription/reducers/followsReducer";
import followersReducer from "./components/subscription/reducers/followersReducer";


const rootReducer = combineReducers({
    authenticationReducer,
    userReducer,
    profileReducer,
    followsReducer,
    followersReducer
});

const persistConfig = {
    key: 'root',
    storage,
}

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
    reducer:{
        persistedReducer
    },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: {
                ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
            }
        })
})

export const persistor = persistStore(store);
export default store;