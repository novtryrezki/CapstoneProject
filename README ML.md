# Modelling AnimalScope 

### Summary
AnimalScope is an animal recognition application which used Convolutional Neural Networks (CNN) models. CNNs are good for pattern recognition and feature detection which is especially useful in image classification. We use Tensorflow and a pre-trained Deep Learning CNN model called MobileNetv2 SSD. This model has been pre-trained for the ImageNet Dataset which contains 1000 classes. 

The program applies Transfer Learning to this existing model and re-trains it to classify a new set of images. We use this model to classify twenty image data sets and returns a output contains the name, characteristics, and facts about them.


## 1. model_ssd.ipynb
### A. Prepare the dataset
1. Download the dataset as a zip from `https://drive.google.com/drive/folders/1yY3oPQJG7JYN4r24czmg2RKdkilAaVZR?usp=sharing` 
2. Unzip downloaded zip file
In order to start the transfer learning process, a folder named images needs to be uploaded in the root of the project folder. This folder will contain the image data sets for all the subjects, for whom the classification is to be performed.

### B. Data Preprocessing
1. Data Integration
Combining data from sources (Kaggle and Images.cv) into a single unified dataset.
2. Data Cleaning
Removing any errors and correcting inconsistent data formats.
3. Data Transformation
Normalizing data, standardizing and scaling and data augmentation.
4. Data Reduction
Features and labels extraction, sampling data.

### C. Modelling Process
1. Define the model. In this scope, the model uses the pre-trained model called SSD MobileNetv2 as the base model. The additional layers use `Conv2D,  GlobalAveragePooling2D, Flatten, and Dropout` layer with 3 layers of Keras which is a Dense layer. The activation function used in the model is `ReLu` for the first 3 additional layers. The last activation function is `softmax` to categorical-class classification
2. Compile the model. The loss function used in this model is the `categorical_crossentropy` (this loss is optimized for categorical-class classification). To fit this model better, `Adaptive Moment Estimation (Adam)` is used for optimization, with the learning rate = `1e-4` and `accuracy` as the metric
3. Fit the model with the `epoch of 25` and validation set as validation data

### D. Evaluation
1. Table of relevant in accuracy and loss

   | Accuracy | Val_accuracy | Loss   | Val_loss |  
   | -------- | ------------ | ------ | -------- |
   | 0.9775   | 0.9435       | 0.0984 | 0.4518   |

2. Evaluation metrics
* Accuracy 0.935 indicates that the model succeeded in correctly predicting about 93.5% of the total sample evaluated.
* Precision 0.938 indicates that the model can correctly predict about 93.8% of all positive predictions made. 
* Recall 0.935 indicates that the model managed to detect about 93.5% of all true positive samples. 
* F1 Score 0.935 indicates that the model has a low error rate and a good ability to classify data.
* Test Loss 0.487 indicates the error level of the model when making predictions on the test data. 
* Test Accuracy 0.935 indicates the level of accuracy of the model when making predictions on test data.
