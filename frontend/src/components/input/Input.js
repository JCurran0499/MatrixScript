import { useState } from 'react'

export const Input = () => {
    const [command, handleCommand] = useState('')
    const [commandResponse, handleCommandResponse] = useState('')

    const processJsonResponse = (json) => {
        if (json.response.matrix) {
            return json.response.matrix
        }
        else {
            return json.response
        }
    }

    const handleSubmit = (e) => {
        e.preventDefault()
        fetch(`http://${process.env.REACT_APP_BACKEND}:4567/`, {
            credentials: 'include', 
            method: 'POST',
            body: `{"command": "${command}"}`,
        })
        .then((res) => res.json())
        .then((json) => processJsonResponse(json))
        .then((resp) => handleCommandResponse(resp));
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input 
                    type="input"
                    value={command}
                    onChange={(e) => handleCommand(e.target.value)}
                />
                <input
                    type="submit"
                />
            </form>
            <h3>{commandResponse}</h3>
        </div>
    )
}
