package com.citi.reghub.rds.scheduler.compression;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.FileSystemUtils;

public class TestZipDirectory implements TestZip {
	private Node<String> root;
	private Path sourcePath;
	private Path destZipPath;

	public TestZipDirectory(String sourceDir) throws IOException {
		String destZipDir = sourceDir + ".zip";
		sourcePath = Paths.get(sourceDir);
		destZipPath = Paths.get(destZipDir);
	}

	public TestZipDirectory(String sourceDir, String destZipDir) throws IOException {
		sourcePath = Paths.get(sourceDir);
		destZipPath = Paths.get(destZipDir);
	}

	public void initialize() throws IOException {
		root = new Node<String>(sourcePath, true);
		setupDirTree();
		createFileTree(root);
	}

	public void clean() throws IOException {
		FileSystemUtils.deleteRecursively(root.getPath().toFile());
		Files.deleteIfExists(destZipPath);
	}

	// setup a in-memory tree structure used to create the directories and files for testing
	private void setupDirTree() throws IOException {
		List<Node<String>> children = new ArrayList<>();

		Path path = root.getPath().resolve("dir1");
		Node<String> node = CreateDirWithChildren(path.toString(), "data1", 2, 3);
		children.add(node);

		path = root.getPath().resolve("dir2");
		node = CreateDirWithChildren(path.toString(), "data2", 3, 2);
		children.add(node);

		path = root.getPath().resolve("dir3");
		node = CreateDirWithChildren(path.toString(), "data3", 4, 1);
		children.add(node);

		root.setChildren(children);
	}

	private void createFile(Path path, String content) throws IOException {
		Files.createFile(path);
		Files.write(path, content.getBytes());
	}

	private void createFile(Node<String> node) throws IOException {
		createFile(node.getPath(), node.getData());
	}

	// create a list of file nodes
	private List<Node<String>> createDataNodes(String pathName, String data, int nodeNum) {
		List<Node<String>> nodeList = new ArrayList<>();
		for (int i = 1; i <= nodeNum; i++) {
			Path filePath = Paths.get(pathName, data + "_" + i + ".txt");
			Node<String> node = new Node<>(filePath, data + "_" + i);
			nodeList.add(node);
		}

		return nodeList;
	}

	// create a list of directory nodes without children
	private List<Node<String>> createDirNodes(String pathName, int nodeNum) {
		List<Node<String>> nodeList = new ArrayList<>();
		for (int i = 1; i <= nodeNum; i++) {
			String fname = Paths.get(pathName).getFileName().toString();
			Path dirPath = Paths.get(pathName, fname + "_" + i);
			Node<String> node = new Node<>(dirPath, true);
			nodeList.add(node);
		}

		return nodeList;
	}

	// make a directory node with children mixed data file and directory nodes
	private Node<String> CreateDirWithChildren(String dir, String data, int dirNum, int dataNum) {
		List<Node<String>> dataNodes = createDataNodes(dir, data, dataNum);
		List<Node<String>> dirNodes = createDirNodes(dir, dirNum);

		dataNodes.addAll(dirNodes); 
		Path nodePath = Paths.get(dir);
		Node<String> node = new Node<>(nodePath, dataNodes, true);

		return node;
	}

	// create the actual directories and files on the disk file system with a Node tree
	private void createFileTree(Node<String> topNode) throws IOException {
		if (topNode == null) {
			return;
		}

		for (Node<String> node : topNode.getChildren()) {
			if (node.isDirectory()) {
				Files.createDirectories(node.getPath());
				if (node.getChildren() != null) {
					createFileTree(node);
				}
			} else {
				createFile(node);
			}
		}
	}
}

// a node of a tree
class Node<T> {
	private Path path = null;
	private T data = null;
	private List<Node<T>> children = null;
	private boolean isDirectory = false;

	public Node(Path path, List<Node<T>> children, boolean isDirectory) {
		this.path = path;
		this.children = children;
		this.isDirectory = isDirectory;
	}

	// construct an empty directory node
	public Node(Path path, boolean isDirectory) {
		this.path = path;
		this.isDirectory = isDirectory;
	}

	// construct a file node
	public Node(Path path, T data) {
		this.path = path;
		this.data = data;
		this.isDirectory = false;
	}

	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}

	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	public List<Node<T>> getChildren() {
		return children;
	}
	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}

	public boolean isDirectory() {
		return isDirectory;
	}
	public void setDirectory(boolean isDir) {
		this.isDirectory = isDir;
	}

	@Override
	public String toString() {
		return "Node [path=" + path + ", data=" + data + ", children=" + children + ", isDirectory=" + isDirectory + "]";
	}
}
