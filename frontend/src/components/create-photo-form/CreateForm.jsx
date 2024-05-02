import React, { useState } from "react";
import "./CreateForm.css";
import axios from "axios";

const CreateForm = ({ handleClose }) => {
  const [file, setFile] = useState(null);
  const [description, setDescription] = useState("");
  const [hashtags, setHashtags] = useState("");

  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];
    setFile(selectedFile);
  };

  const handleDescriptionChange = (event) => {
    setDescription(event.target.value);
  };

  const handleHashtagsChange = (event) => {
    setHashtags(event.target.value);
  };

  async function handleSubmit(e) {
    e.preventDefault();
    const photoInfo = {
      file: file,
      description: description,
      hashtags: hashtags.str.split(/\s+/),
      isAvatar: false,
    };
    const response = await axios.post(
        "localhost:8080/api/v1/photos",
        photoInfo
    );
  }
  const handleCloseButtonClick = () => {
    handleClose(); // Вызов функции обратного вызова из props
  };
  return (
      <div className={"create_form_global"}>
        <div className={"createForm__inner"}>
          <div className={"header"} onClick={handleCloseButtonClick}>
            X
          </div>
          <div className={"body_content"}>
            <div className={"body_contentChooseFile"}>
              <label htmlFor="file">Выберите файл:</label>
              <input type="file" id="file" onChange={handleFileChange} />
            </div>
            <div>
              <div>
                <label htmlFor="description">Описание:</label>
                <textarea
                    id="description"
                    value={description}
                    onChange={handleDescriptionChange}
                />
              </div>
              <div>
                <label htmlFor="hashtags">Хештеги:</label>
                <input
                    type="text"
                    id="hashtags"
                    value={hashtags}
                    onChange={handleHashtagsChange}
                />
                <button
                    onClick={handleCloseButtonClick}
                    type="submit"
                    onSubmit={handleSubmit}
                >
                  Опубликовать
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
  );
};

export default CreateForm;
