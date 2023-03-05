import { useState } from 'react'
import axios from 'axios'

export const Input = (props) => {
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
        axios({
            method: 'post',
            url: `http://${process.env.REACT_APP_BACKEND}:4567/`,
            params: {
                'token': props.sessionToken
            },
            data: {
              command: command
            },
            withCredentials: true
        })
        .then((res) => {
            console.log(res.headers)
            return res
        })
        .then((res) => res.data)
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
