import './History.css'

export const History = (props) => {
    const fullText = props.text.length > 0

    const pList = (() => {
        const pList = []
        if (props.isMatrix) {
            const textList = props.text.split("n")
            for (let i = 0; i < textList.length; i++) {
                pList.push(
                    <div key={pList.length} className="history">
                        <pre>{textList[i]}</pre>
                        {textList[i].length > 0 && <br/>}
                    </div>
                )
            }
        }
        else {
            pList.push(
                <div key={0} className="history">
                    <pre>{props.text}</pre>
                    {props.text.length > 0 && <br/>}
                </div>
            )
        }

        return pList
    })()


    return (
        <div>
            {props.isCommand && (
                <p id="cursor">{">> "}</p>
            )}

            {pList}

            {!props.isCommand && fullText && (
                <br/>
            )}
        </div>
    )
}