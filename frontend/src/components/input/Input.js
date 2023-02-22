import { useState } from 'react'

export const Input = () => {
    const [commandResponse, handleCommandResponse] = useState('')

    const handleJsonResponse = (json) => {
        if (json.response.matrix) {
            return json.response.matrix
        }
        else {
            return json.response
        }
    }

    fetch(`http://${process.env.REACT_APP_BACKEND}:4567/`, {
        credentials: 'same-origin', 
        method: 'POST',
        body: '{"command": "[1 2 3 ; 4 5 6]"}',
    })
    .then((res) => res.json())
    .then((json) => handleJsonResponse(json))
    .then((resp) => handleCommandResponse(resp));

    return (
        <h3>{commandResponse}</h3>
    )
}
