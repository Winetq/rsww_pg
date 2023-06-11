import { useState } from 'react';
import Toast from 'react-bootstrap/Toast';


function InfoToast({ variant, content, onClose }) {

    let [show, setShow] = useState(true);
    const toggleShow = () => {
        setShow(!show);
        if(onClose instanceof Function)
            onClose();
    }

    return (
        <Toast show={show} onClose={toggleShow} className="position-fixed bottom-0 end-0 m-3">
            <Toast.Header className={"fw-bold " + (
                variant === 'success' ? "bg-success" : 
                variant === 'warning' ? "bg-warning" : 
                variant === 'danger' ? "bg-danger" :
                "bg-secondary"
            )}>
                <strong className="me-auto text-white">
                    {
                        variant === 'success' ? "Success" :
                        variant === 'warning' ? "Warning" :
                        variant === 'danger' ? "Error" :
                        "Alert"
                    }
                </strong>
            </Toast.Header>
            <Toast.Body>
                {content}
            </Toast.Body>
        </Toast>
    );
}

export default InfoToast;