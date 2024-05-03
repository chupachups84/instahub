import React, { useState } from "react";
import "./CreateForm.css";
import axios from "axios";

const CreateForm = ({ handleClose }) => {
  const [file, setFile] = useState(null);
  const [description, setDescription] = useState("");
  const [hashtags, setHashtags] = useState("");

  const handleFileChange = (event) => {


    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = (event) => {
      const imageUrl = event.target.result;
      setFile(imageUrl);
    };

    reader.readAsDataURL(file);
  };

  const handleDescriptionChange = (event) => {
    const inputString = event.target.value;
    const regex = /#(\w+)/g;
    const hashtags = inputString.match(regex);
    const hashtagsArray = hashtags ? hashtags.map(tag => tag.substring(1)) : [];
    setHashtags(hashtagsArray);
    setDescription(inputString);
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
              {file && (
                  <img className={"img-preview"} src={file} alt="Uploaded" />
              )}
            </div>
            <div>
              <div>
                <label htmlFor="description">Описание:</label>
                <textarea className={"create_form_description"}
                    id="description"
                    value={description}
                    onChange={handleDescriptionChange}
                />
              </div>
              <div>
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
