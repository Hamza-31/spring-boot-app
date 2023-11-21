
import { getCustomers } from "./_services/client.js";
import SidebarWithHeader from "./_shared/SidebarWithHeader.jsx";
import { Spinner, Text, Wrap, WrapItem } from '@chakra-ui/react'
import { useEffect, useState } from "react";
import CardWithImage from "./components/CardWithImage.jsx"
import DrawerForm from "./components/DrawerForm.jsx";
import { errorNotification } from "./_services/notification.js";
function App() {
	const [customers, setCustomers] = useState([])
	const [loading, setLoading] = useState(false)
	const [error, setError] = useState("");
	const fetchCustomers = () => {
		setLoading(true)
		getCustomers().then(res => {
			setCustomers(res.data)
		}).catch(err => {
			setError(err.response.data.message)
			errorNotification(
				err.code,
				err.response.data.message
			)
		}).finally(() => {
			setLoading(false)
		})
	}
	useEffect(() => {
		fetchCustomers();
	}, [])
	if (loading) {
		return (
			<SidebarWithHeader >
				<Spinner
					thickness='4px'
					speed='0.65s'
					emptyColor='gray.200'
					color='blue.500'
					size='xl'
				/>
			</SidebarWithHeader>
		)
	}
	if (customers.length <= 0) {
		return (
			<SidebarWithHeader >
				<DrawerForm fetchCustomers={fetchCustomers} />
				<Text mt={5}>No Customers available</Text>
			</SidebarWithHeader>
		)
	}
	if (error) {
		return (
			<SidebarWithHeader >
				<DrawerForm fetchCustomers={fetchCustomers} />
				<Text mt={5}>Ooops there is an error !</Text>
			</SidebarWithHeader>
		)
	}
	return (
		<>
			<SidebarWithHeader >
				<DrawerForm fetchCustomers={fetchCustomers} />
				<Wrap justify='center' spacing="30px">
					{customers.map((customer, index) => (
						<WrapItem key={index}>
							<CardWithImage fetchCustomers={fetchCustomers} imageNumber={index} {...customer} />
						</WrapItem>
					))}
				</Wrap>
			</SidebarWithHeader>
		</>

	)
}

export default App
