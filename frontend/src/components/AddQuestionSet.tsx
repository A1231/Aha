import { useState } from "react";
import type { Question } from "../types/Question";
import AddQuestion from "./AddQuestion";
import Button from "./Button";
import Notification from "./Notification";
import { API_BASE_URL } from "../config";

function AddQuestionSet({ onSubmitted }: { onSubmitted: () => void }) {
    const [questions, setQuestions] = useState<Question[]>([]);
    const [error, setError] = useState<string | null>(null);

    const addQuestion = () => {
        setQuestions([...questions, { text: "", options: ["", "", "", ""], correctOptionIndex: 0 }]);
    };

    const removeQuestion = (index: number) => {
        setQuestions(questions.filter((_, i) => i !== index));
    };

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError(null);
        try {
            const response = await fetch(`${API_BASE_URL}/api/rooms/questions`, {
                method: "POST",
                body: JSON.stringify({ questions }),
                headers: { "Content-Type": "application/json" },
            });
            if (!response.ok) {
                const data = await response.json().catch(() => null);
                setError(data?.message ?? "Failed to submit questions");
                return;
            }
            onSubmitted();
        } catch {
            setError("Network error. Please check your connection.");
        }
    };

    return (
        <div className="card">
            {error && <Notification message={error} variant="error" onDismiss={() => setError(null)} />}
            <h2 className="card__title">Add Questions</h2>
            <form className="form" onSubmit={handleSubmit}>
                <div className="question-set">
                    {questions.map((question, index) => (
                        <AddQuestion
                            key={index}
                            question={question}
                            index={index}
                            setQuestions={setQuestions}
                            questions={questions}
                        />
                    ))}
                </div>
                <div className="form-actions form-actions--wrap">
                    <Button type="button" variant="secondary" onClick={addQuestion}>
                        + Add Question
                    </Button>
                    {questions.length > 0 && (
                        <Button
                            type="button"
                            variant="danger"
                            onClick={() => removeQuestion(questions.length - 1)}
                        >
                            Remove Last
                        </Button>
                    )}
                    {questions.length > 0 && (
                        <Button type="submit" variant="success">
                            Submit Questions
                        </Button>
                    )}
                </div>
            </form>
        </div>
    );
}

export default AddQuestionSet;
