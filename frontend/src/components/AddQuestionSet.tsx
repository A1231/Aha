import { useState } from "react";
import type { Question } from "../types/Question";
import AddQuestion from "./AddQuestion";
import Button from "./Button";
import { useNavigate } from "react-router-dom";

function AddQuestionSet() {

    const navigate = useNavigate();
    const [questions, setQuestions] = useState<Question[]>([]);
    const [submitted, setSubmitted] = useState(false);

    const addQuestion = () => {
        setQuestions([...questions, { text: "", options: ["", "", "", ""], correctOptionIndex: 0 }]);
    }
    const removeQuestion = (index: number) => {
        setQuestions(questions.filter((_, i) => i !== index));
    }
    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        
        console.log(questions);
        const response = await fetch("http://localhost:8080/api/rooms/questions", {
            method: "POST",
            body: JSON.stringify({ questions }),
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include"
        })
        if (!response.ok){
            alert("Failed to submit questions");
            return;
        }
        setSubmitted(true);
        const data = await response.json();
        console.log(data);
    }

  

    return (
        <>
        {!submitted && <form onSubmit={handleSubmit}>
            {questions.map((question, index) => (
                <AddQuestion key={index} question={question} index={index} setQuestions={setQuestions} questions={questions} />
            ))}
            <Button type="button" onClick={addQuestion}>Add Question</Button>
            <Button type="button" onClick={() => removeQuestion(questions.length - 1)}>Remove Question</Button>
            <Button type="submit">Submit Questions</Button>
        </form>
        }
        {submitted && <div>Questions submitted successfully</div>}
        {submitted && <Button type="button" onClick={() => navigate("/game-started")}>Start Game</Button>}
        </>
    )
}
export default AddQuestionSet;