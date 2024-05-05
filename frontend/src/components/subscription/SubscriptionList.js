import './SubscriptionList.css'
import {useNavigate} from "react-router-dom";
import {button} from "react-validation/build/main";

const SubscriptionList = ({ subscriptions, action, modalSetter }) => {
    const { fetchFunction, nextPage } = action;
    const {setActive} = modalSetter

    const handleLoadMore = () => {
        fetchFunction(nextPage);
    };
    const handleNavigate = () => {
        setActive(false)
        const token = JSON.parse(localStorage.getItem('user'))
        localStorage.clear()
        localStorage.setItem('user',JSON.stringify(token))
    }
    const navigate = useNavigate();
    return (
        <>
            <div>
                {subscriptions.map(subscription => (
                    <div
                        key={subscription.username}
                        className={'sub-container'}
                        onClick={()=>{

                            handleNavigate();
                            navigate('/'+subscription.username)
                        }}
                    >
                        <div className={'sub-container__top'}>
                            <div className={'sub-container__top__avatar'}></div>
                            <div className={'sub-container__top__username'}>
                                {subscription.username.length > 13 ? subscription.username.slice(0, 14) + '...' : subscription.username}
                            </div>
                        </div>
                        <div className={'sub-container__bottom'}>
                            <div
                                className={'sub-container__bottom__fullname'}>{subscription.lastName + ' ' + subscription.firstName}
                            </div>
                        </div>
                    </div>
                ))}
            </div>
            <div className={'button-container'}>
                <button className={'loading-button'} onClick={handleLoadMore}>load more</button>
            </div>
        </>
    );
};
export default SubscriptionList;