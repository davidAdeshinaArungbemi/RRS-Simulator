// @Override
// protected Object doInBackground() throws Exception {

// if (sourceDir.isDirectory()) {

// // if directory not exists, create it
// if (!destDir.exists()) {
// destDir.mkdir();
// publish("Folder " + sourceDir.getName() + " was created");
// }

// // list all the directory contents
// String files[] = sourceDir.list();

// for (String file : files) {
// // construct the src and dest file structure
// File srcFile = new File(sourceDir, file);
// File destFile = new File(destDir, file);
// // recursive copy
// copyFolder(srcFile, destFile);
// }

// } else {
// try {
// copyFile(sourceDir, destDir);
// } catch (Exception e) {
// }
// }

// return null;

// }
