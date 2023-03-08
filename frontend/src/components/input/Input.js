import { useState, useEffect } from 'react'
import axios from 'axios'
import './Input.css'

export const Input = (props) => {
    const [command, handleCommand] = useState('')
    const [commandResponse, handleCommandResponse] = useState({})
    const [triggerCommand, toggleTriggerCommand] = useState(false)
    const [triggerCommandResponse, toggleTriggerCommandResponse] = useState(false)
    

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
        .then((res) => res.data)
        //.then((json) => processJsonResponse(json))
        .then((json) => handleCommandResponse(json))
        .then(() => toggleTriggerCommand(true))
    }

    /* Waterfall Effect */
    useEffect(() => {
        if (triggerCommand) {
            props.onSubmitCommand(command)
            handleCommand('')
            
            toggleTriggerCommandResponse(true)
        }
    }, [triggerCommand])

    useEffect(() => {
        if (triggerCommandResponse) {
            props.onSubmitCommandResponse(commandResponse)

            toggleTriggerCommand(false)
            toggleTriggerCommandResponse(false)
        }
    }, [triggerCommandResponse])
    /* ----------------- */


    return (
        <div>
            <p>{">> "}</p>
            <form onSubmit={handleSubmit}>
                <input
                    type="input"
                    value={command}
                    onChange={(e) => handleCommand(e.target.value)}
                    autoFocus
                />
                <br/>
            </form>
        </div>
    )
}
