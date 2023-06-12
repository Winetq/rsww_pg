import { faBell } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useState } from 'react';
import Toast from 'react-bootstrap/Toast';


function NotificationToast({ variant, content, onClose }) {

    let [show, setShow] = useState(true);
    const toggleShow = () => {
        setShow(!show);
        if(onClose instanceof Function)
            onClose();
    }

    return (
        <Toast show={show} onClose={toggleShow} autohide={true} delay={1000} className="position-fixed top-0 end-0 m-3">
            <Toast.Header closeButton={false} className={"fw-bold " + (
                variant === 'success' ? "bg-success" : 
                variant === 'warning' ? "bg-warning" : 
                variant === 'danger' ? "bg-danger" :
                "bg-secondary"
            )}>
                <div className="w-100 d-flex justify-content-between">
                    <div className="fw-bold text-white">
                        {
                            variant === 'success' ? "Success" :
                            variant === 'warning' ? "Warning" :
                            variant === 'danger' ? "Error" :
                            "Alert"
                        }
                    </div>
                    <div className="text-white">
                       <FontAwesomeIcon icon={faBell} className="fa-shake fa-xl" /> 
                    </div>
                </div>
            </Toast.Header>
            <Toast.Body>
                {content}
            </Toast.Body>
        </Toast>
    );
}

export default NotificationToast;