# E-commerce prototype - Class 1

![Alt text](<Skærmbillede 2024-05-24 kl. 10.55.05.png>)

## About The Project
### Arne's Electronics
*Arne’s electronics is a medium sized Danish electronics store, founded in 2003. They sell products such as desktop PCs, laptops, and monitors, but have recently also branched out to include PC components and helpful information pages about topics such as how to change your CPU, choosing a laptop for school, and the difference between display types.
Arne’s electronics has made an order for an E-commerce solution (webshop), and they need a full, offline prototype as a proof of concept.*


## Getting Started

### Prerequisites

#### Demo mode - Shop
The shop module has a demo mode that uses dummy data to showcase the shop, to enable this mode, the flag in the 'App' class in the SHOP component to 'true':

```
Flags.setDevelopment(true);
```
---

#### Full mode
To run this project with all the components together, you have to setup some local databases for each of the following subcomponents:

#### DAM
>1. For the DAM you need to have a local PostgreSQL database running.
>
>2. You then need to update the settings in the 'db-config.properties' file. These need to reflect the connection to you local database.
>
>3. Finally you need to run the 'db.sql' code in your database, to setup the correct data.
#### CMS
>1. For the CMS you need to have a local MongoDB database running.
>
>2. You then need to update the settings in the 'MongoDBMethods.java' class to reflect your database information.
>
>3. You can then run the 'MainCMS' class to add new layouts to the system.
#### PIM
>1. For the PIM you need to have a local PostgreSQL database running.
>
>2. You then need to update the settings in the 'Communicator.java' class to reflect your database information.
>
>3. Finally you need to run the 'PIM_Database.SQL' and the 'ProductDataSHOP.SQL' in your database, to setup the correct data.

After setting up the databases, the development flag should be set to false in the App class of the SHOP component:

```
Flags.setDevelopment(false);
```